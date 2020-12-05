(ns circles.db
  (:require [circles.circle :as circle]))

(defn selected [db]
  (get-in db [:circles (:selected db)]))

(defn displayed [db events]
  (->> (:events events)
       (filter #(= (:event %) :click))
       (map (fn [c] {(:id c) (get-in db [:all-circles (:id c)])}))
       (apply merge)))

(defn rebuild [db events]
  (swap! db assoc :circles (displayed @db @events)))

(defn apply-scale [db scale direction]
  (let [selected-circle (get-in @db [:all-circles (:circle-id scale)])]
    (swap! db assoc-in
           [:all-circles (:circle-id scale)]
           (circle/with-radius (direction scale) selected-circle))))
