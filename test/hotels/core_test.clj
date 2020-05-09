(ns hotels.core-test
  (:require [clojure.test :refer :all]
            [hotels.core :refer :all]))

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
         (interpret_booking_dates  ["Regular", "16Mar2009(mon),17Mar2009(tues),18Mar2009(wed)"]))
      "given a string of dates with commas, it should return a list of dates"))

(deftest parse-bookings-test
  (is (= ["Regular", "16Mar2009(mon)", "17Mar2009(tues)", "18Mar2009(wed)"]
         (parse_bookings "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"))
      "given a dsadas of dates with commas, it should return a list of dates"))