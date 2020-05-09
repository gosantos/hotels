(ns hotels.core
  (:require [clojure.string :as str]))

(defn remove_white_spaces
  [input]
  (str/replace input #"\s+" ""))

(defn interpret_customer
  [input]
  (str/split input #":"))

(defn interpret_booking_dates
  [input]
  (flatten (mapv #(str/split % #",") input)))

(defn parse_bookings [input] ((comp interpret_booking_dates interpret_customer remove_white_spaces) input))

(let [])

(def person
  {:customerType "Kelly"
   :listOfDates "Keen"
   :age 32
   :occupation "Programmer"})
