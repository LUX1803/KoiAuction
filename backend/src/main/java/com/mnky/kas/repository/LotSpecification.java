package com.mnky.kas.repository;

import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Lot;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class LotSpecification {
    public static Specification<Lot> hasAuctionId(Short auctionId) {
        return (root, query, cb) -> {
            if (auctionId == null) {
                return null;
            }
            return cb.equal(root.get("auction").get("id"), auctionId);
        };
    }



    public static Specification<Lot> hasVarietyNames(List<String> varietyNames) {
        return (root, query, cb) -> {
            if (varietyNames == null || varietyNames.isEmpty()) {
                return null;
            }
            return cb.lower(root.get("koi").get("variety").get("name"))
                    .in(varietyNames.stream()
                            .map(String::toLowerCase)
                            .toList());
        };
    }

    public static Specification<Lot> hasLength(Short length) {
        return (root, query, cb) -> {
          if (length == null) {
              return null;
          }
          return cb.equal(root.get("koi").get("length"), length);
        };
    }

    public static Specification<Lot> hasGender(String gender) {
        return (root, query, cb) -> {
            if (gender == null) {
                return null;
            }
            try {
                Gender genderEnum = Gender.valueOf(gender.toUpperCase());
                return cb.equal(root.get("koi").get("gender"), genderEnum);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    public static Specification<Lot> hasFarmNames(List<String> farmNames) {
        return (root, query, cb) -> {
            if (farmNames == null || farmNames.isEmpty()) {
                return null;
            }
            return cb.lower(root.get("koi").get("farm").get("name"))
                    .in(farmNames.stream()
                            .map(String::toLowerCase)
                            .toList());
        };
    }

    public static Specification<Lot> hasMethodNames(List<String> methodNames) {
        return (root, query, cb) -> {
            if (methodNames == null || methodNames.isEmpty()) {
                return null;
            }
            return cb.lower(root.get("method").get("name"))
                    .in(methodNames.stream()
                            .map(String::toLowerCase)
                            .toList());
        };
    }

    public static Specification<Lot> hasPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            } else if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("startingPrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("startingPrice"), minPrice);
            } else {
                return cb.lessThanOrEqualTo(root.get("startingPrice"), maxPrice);
            }
        };
    }
}
