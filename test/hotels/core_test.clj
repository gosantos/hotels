(ns hotels.core-test
  (:require [clojure.test :refer :all]
            [hotels.core :refer :all]
            [java-time :as time]))

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
  (is (= ["Regular", "16Mar2009(mon)", "17Mar2009(tues)", "18Mar2009(wed)"]
         (parse_bookings "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"))
      "given a dsadas of dates with commas, it should return a list of dates"))

(deftest format-date-test
  (is (= (time/local-date 2009 03 16)
         (format_date "16Mar2009(mon)"))
      "given a date as string, it should convert to a local-date object")
  (is (= (time/local-date 2009 03 17)
         (format_date "17Mar2009(tues)"))
      "given another date as string, it should convert to a local-date object"))

(deftest test-hotels-size
  (is (= 270
         (hotel_price_service [(time/local-date 2009 03 16) (time/local-date 2009 03 17) (time/local-date 2009 03 18)]))
      "given a hotel and a list of dates, it should return the price"))