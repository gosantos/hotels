(ns hotels.core
  (:use clojure.pprint)
  (:require [clojure.string :as str]
            [java-time :as time]))

;; Domain
(defrecord Hotel [name rating rates])
(defrecord Rates [regular_rate rewards_rate])
(defrecord Rate [weekend weekday])
(defrecord Booking [customer_type list_of_dates])

(def Lakewood (Hotel. "Lakewood" 3 (Rates. (Rate. 110 90) (Rate. 80 80))))
(def Bridgewood (Hotel. "Bridgewood" 4 (Rates. (Rate. 160 60) (Rate. 110 50))))
(def Ridgewood (Hotel. "Ridgewood" 5 (Rates. (Rate. 220 150) (Rate. 100 40))))
(def Hotels [Lakewood Bridgewood Ridgewood])

;; Date Functions
(defn number-of-weekday-days [dates]
  (count (filter time/weekday? dates)))

(defn number-of-weekend-days [dates]
  (count (filter time/weekend? dates)))

(defn remove-day-of-week [date]
  (str/replace date #"\(.{3,4}\)" ""))

(defn format_date [date]
  (time/local-date "ddMMMyyyy" (remove-day-of-week date)))

;; Client
(defn read_booking_requests []
  (str/split (slurp "resources/booking-requests") #"\n"))

;; Parser
(defn remove_white_spaces [input]
  (comp (str/replace input #"\s+" "")))

(defn interpret_customer [input]
  (str/split input #":"))

(defn interpret_booking_dates [input]
  (flatten
    (mapv #(str/split % #",") input))
  )

(defn parse_booking_request [input]
  (let [booking_request ((comp interpret_booking_dates interpret_customer remove_white_spaces) input)]
    (Booking.
      (first booking_request)
      (mapv format_date (rest booking_request)))
    )
  )

;; Service
(defn hotel_regular_price_service [hotel dates]
  {
   :name   (-> hotel :name)
   :rating (-> hotel :rating)
   :price  (+
             (* (-> hotel :rates :regular_rate :weekend) (number-of-weekend-days dates))
             (* (-> hotel :rates :regular_rate :weekday) (number-of-weekday-days dates)))
   })

(defn hotel_rewards_price_service [hotel dates]
  {
   :name   (-> hotel :name)
   :rating (-> hotel :rating)
   :price  (+
             (* (-> hotel :rates :rewards_rate :weekend) (number-of-weekend-days dates))
             (* (-> hotel :rates :rewards_rate :weekday) (number-of-weekday-days dates)))
   })

(defn by-price-and-rating [x y]
  (compare [(:price x) (:rating y)]
           [(:price y) (:rating x)]))


(defn hotels_price_service [booking_request]
  (first (sort by-price-and-rating
               (if (= (-> booking_request :customer_type) "Regular")
                 (mapv #(hotel_regular_price_service % (-> booking_request :list_of_dates)) Hotels)
                 (mapv #(hotel_rewards_price_service % (-> booking_request :list_of_dates)) Hotels)
                 )
               )
         )
  )

(defn booking_service []
  (let [booking_requests (mapv #(parse_booking_request %) (read_booking_requests))]
    (mapv #(hotels_price_service %) booking_requests))
  )
