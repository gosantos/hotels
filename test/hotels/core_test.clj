(ns hotels.core-test
  (:require [clojure.test :refer :all]
            [hotels.core :refer :all]
            [java-time :as time])
  (:import (hotels.core Booking)))

(deftest remove-white-spaces-test
  (is (= "Regular:16Mar2009(mon),17Mar2009(tues),18Mar2009(wed)"
         (remove_white_spaces "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"))
      "given a string, it should remove all the whitespaces"))

(deftest interpret-customer-test
  (is (= ["Regular", "16Mar2009(mon),17Mar2009(tues),18Mar2009(wed)"]
         (interpret_customer "Regular:16Mar2009(mon),17Mar2009(tues),18Mar2009(wed)"))
      "given a string with colon, it should return a customer and a string with something else"))

(deftest interpret-booking-dates-test
  (is (= ["Regular" "16Mar2009(mon)" "17Mar2009(tues)" "18Mar2009(wed)"]
         (interpret_booking_dates ["Regular", "16Mar2009(mon),17Mar2009(tues),18Mar2009(wed)"]))
      "given a string of dates with commas, it should return a list of dates"))

(deftest parse-bookings-test
  (is (= (Booking. "Regular" [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)])
         (parse_booking_request "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"))
      "given a dsadas of dates with commas, it should return a list of dates"))

(deftest format-date-test
  (is (= (time/local-date 2009 03 16)
         (format_date "16Mar2009(mon)"))
      "given a date as string, it should convert to a local-date object")
  (is (= (time/local-date 2009 03 17)
         (format_date "17Mar2009(tues)"))
      "given another date as string, it should convert to a local-date object"))

(deftest hotel-regular-price-service-test
  (is (= {:name "Lakewood", :rating 3, :price 270}
         (hotel_regular_price_service Lakewood [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)]))
      "given a hotel and a list of dates, it should return the price"))

(deftest hotel-rewards-price-service-test
  (is (= {:name "Lakewood", :rating 3, :price 240}
         (hotel_rewards_price_service Lakewood [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)]))
      "given a hotel and a list of dates, it should return the price"))

(deftest hotels-price-service-test
  (is (= {:name "Bridgewood", :rating 4, :price 180}
         (hotels_price_service Hotels (Booking. "Regular" [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)])))
      "given a list of hotels a booking request, it should return the prices")
  (is (= {:name "Ridgewood", :rating 5, :price 120}
         (hotels_price_service Hotels (Booking. "Rewards" [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)])))
      "given a list of hotels a booking request, it should return the prices"))

(deftest read-booking-requests-test
  (is (= ["Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)" "Regular: 20Mar2009(fri), 21Mar2009(sat), 22Mar2009(sun)" "Rewards: 26Mar2009(thur), 27Mar2009(fri), 28Mar2009(sat)"]
         (read_booking_requests))
      "given a hotel and a list of dates, it should return the price"))

(def expected-sorted-list-of-prices
  [{:name "Hotel1", :price 9, :rating 2}
   {:name "Hotel2", :price 9, :rating 1}
   {:name "Hotel3", :price 10, :rating 5}
   {:name "Hotel4", :price 10, :rating 4}
   {:name "Hotel5", :price 10, :rating 3}
   {:name "Hotel6", :price 50, :rating 5}])

(def actual-list-of-price
  [{:name "Hotel6", :price 50, :rating 5}
   {:name "Hotel4", :price 10, :rating 4}
   {:name "Hotel3", :price 10, :rating 5}
   {:name "Hotel5", :price 10, :rating 3}
   {:name "Hotel2", :price 9, :rating 1}
   {:name "Hotel1", :price 9, :rating 2}])

(deftest sort-hotels-by-price-and-rating-service-test
  (is (= expected-sorted-list-of-prices
         (sort by-price-and-rating actual-list-of-price))
      "given a collection of hotels with prices and ratings, it should get the cheapest one the best rating"))

(deftest booking-service-test
  (is (= [(Booking. "Regular" [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)])
          (Booking. "Regular" [(time/local-date 2009 03 20) (time/local-date 2009 03 21) (time/local-date 2009 03 22)])
          (Booking. "Rewards" [(time/local-date 2009 03 26) (time/local-date 2009 03 27) (time/local-date 2009 03 28)])]
         (booking_service))
      "should return a list of booking requests"))
