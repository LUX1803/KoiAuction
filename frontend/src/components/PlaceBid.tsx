import { useEffect, useState } from "react";
import { LotDetailProps } from "./LotDetail";
import { Button, Input } from "./ui";
import { Bid } from "@/type/bid.d";
import { buyLotNow, getBidByLotIdAndBidderId, getHighestBidByLotId, placeDutchBid, placeFixedPriceBid, placeSealedBid } from "@/service/bidService";
import { useAuth } from "@/context/AuthContext";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { useToast } from "@/hooks/use-toast";



const PlaceBid = ({ lotDetail }: { lotDetail: LotDetailProps }) => {

  const { user } = useAuth();
  const [highestBid, setHighestBid] = useState<Bid>();
  const [bidAmount, setBidAmount] = useState<number>(lotDetail.startingPrice);
  const [isPlacedBid, setIsPlacedBid] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const { toast } = useToast();


  // Fetch bid details and check if the user has placed a bid
  useEffect(() => {
    const fetchBidData = async () => {
      try {
        if (lotDetail.methodId == 3) {
          const highestBidData = await getHighestBidByLotId(lotDetail.lotId);
          setHighestBid(highestBidData);
          const initialBidAmount = highestBidData
            ? highestBidData.amount + lotDetail.priceInterval
            : lotDetail.startingPrice;
          setBidAmount(initialBidAmount);

          // Check if the current user is the highest bidder
          setIsPlacedBid(highestBidData?.bidderId === user?.id);
        } else {
          const userBid = await getBidByLotIdAndBidderId(lotDetail.lotId, user.id);
          setHighestBid(userBid);
          // Check if the current user has already placed a bid
          setIsPlacedBid(userBid?.bidderId === user?.id);

        }
      } catch (error) {
        console.error("Error fetching bid data:", error);
      }
    };

    fetchBidData();
  }, []);

  // listen highest bid
  useEffect(() => {
    if (lotDetail.status == 'LIVE') {
      const socket = new SockJS("http://localhost:8080/ws");
      const client = Stomp.over(socket);

      client.connect({}, () => {
        console.log("Connected to WebSocket for bid list");

        client.subscribe(`/topic/lot/${lotDetail.lotId}/bid`, (message) => {
          const data = JSON.parse(message.body);
          setHighestBid(data);
          if (data.bidderId == user.id) {
            setIsPlacedBid(true);
          } else {
            setIsPlacedBid(false);
          }
          setBidAmount(data.amount + lotDetail.priceInterval);
        });
      });
      return () => {
        client.disconnect();
      };
    }
  }, [])


  const placeEnglishBid = () => {
    // Get the JWT from localStorage
    const token = localStorage.getItem('token');

    // Send the bid request with the amount
    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      if (validateBid()) {
        stompClient.send(`/app/lot/${lotDetail.lotId}/english-bid`, {
          Authorization: `Bearer ${token}`,
        }, JSON.stringify({
          amount: bidAmount,
        }));
      }

    });
  };

  const placeBid = async () => {
    try {
      switch (lotDetail.methodId) {
        case 1:
          await placeFixedPriceBid(lotDetail.lotId);
          break
        case 2:
          if (validateBid()) {
            await placeSealedBid(lotDetail.lotId, bidAmount);
          } else {
            toast({
              variant: "destructive",
              title: "Invalid bid",
              description: error || "Your bid is invalid",
            });
          }
          break;
        case 3:
          if (validateBid()) {
            await placeEnglishBid();
          } else {
            toast({
              variant: "destructive",
              title: "Invalid bid",
              description: error || "Your bid is invalid",
            });
          }
          break;
      }

    } catch (error) {
      console.error("Error placing bid:", error);
    }
  };



  const validateBid = () => {
    console.log(bidAmount)
    if (!Number(bidAmount)) {
      setError("Please enter a valid bid amount");
      return false;
    }

    if (highestBid && bidAmount < highestBid.amount + lotDetail.priceInterval) {
      setError("Bid amount should be greater than the current highest bid");
      return false;
    } else if (bidAmount < lotDetail.startingPrice) {
      if (lotDetail.methodId == 3 && bidAmount >= lotDetail.buyNowPrice) {
        if (highestBid && bidAmount < highestBid.amount + lotDetail.priceInterval) {
          setError("Bid amount should be greater than the current highest bid");
        } else {
          setError("Bid amount cannot be less than the starting price and more than buy now price");
        }
      } else {
        setError("Bid amount cannot be less than the starting price");
      }
      return false;
    }

    return true;
  };

  const handleBuyNow = () => {
    if (lotDetail.methodId == 4) {
      placeDutchBid(lotDetail.lotId);

    } else {
      buyLotNow(lotDetail.lotId);
    }
  };
  return (
    <>
      {(lotDetail.methodId === 3) && (
        <Button
          className="px-4 py-2 rounded-full bg-purple-300 text-lg"
          onClick={handleBuyNow}>
          Buy now: {lotDetail.buyNowPrice}
        </Button>
      )}
      {isPlacedBid ? (
        <>
          <div className="">

          </div>

        </>
      ) : (
        <>
          {
            lotDetail.methodId === 1 && (<Button
              onClick={placeBid}
              className="px-4 py-2 rounded-full bg-purple-300 text-lg">
              Place bid: {lotDetail.startingPrice}
            </Button>)
          }
          {
            (lotDetail.methodId === 4) && (
              <Button
                onClick={handleBuyNow}
                className="px-4 py-2 rounded-full bg-purple-300 text-lg">
                Buy now: {lotDetail.buyNowPrice}
              </Button>
            )
          }
          {(lotDetail.methodId === 2 || lotDetail.methodId === 3) && (
            <>
              <div className="flex px-4 py-2">
                <Input
                  className="inline rounded-2xl w-40"
                  type="text"
                  value={bidAmount}
                  onChange={(e) => setBidAmount(e.target.value)}
                />
                <Button
                  onClick={placeBid}
                  className="bg-black text-white rounded-full ml-4"
                >
                  Place bid
                </Button>
              </div>
            </>
          )}
        </>
      )}
    </>
  );
};

export default PlaceBid;
