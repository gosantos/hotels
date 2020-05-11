(ns hotels.core
  (:require [clojure.string :as str]
            [java-time :as time]))

;; Parser
(defn remove_white_spaces
  [input]
  (str/replace input #"\s+" ""))

(defn interpret_customer
  [input]
  (str/split input #":"))

(defn interpret_booking_dates
  [input]
  (flatten (mapv #(str/split % #",") input)))

(defn parse_bookings
  [input]
  ((comp interpret_booking_dates interpret_customer remove_white_spaces) input))

;; Date
(defn remove-day-of-week
  [date]
  (str/replace date #"\(.{3,4}\)" ""))

(defn format_date
  [date]
  (time/local-date "ddMMMyyyy" (remove-day-of-week date)))

;; Domain
(defrecord Hotel [name rating rates])
(defrecord Rates [regular_rate rewards_rate])
(defrecord Rate [weekend weekday])
(defrecord Booking [customerType listOfDates])

;; Repository
(def Lakewood (Hotel. "Lakewood" 3 (Rates. (Rate. 110 90) (Rate. 80 80))))
(def Bridgewood (Hotel. "Bridgewood" 4 (Rates. (Rate. 160 60) (Rate. 110 50))))
(def Ridgewood (Hotel. "Ridgewood" 5 (Rates. (Rate. 220 150) (Rate. 100 40))))
(def all_hotels [Lakewood  Bridgewood  Ridgewood])

