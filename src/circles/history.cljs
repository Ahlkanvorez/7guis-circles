(ns circles.history
  (:require [circles.db :as db]
            [circles.event :as event]))

(defn handle-historic-scale [circles events traverse-history direction]
  (let [event (traverse-history events)]
    (when (= :scale (:event event))
      (db/apply-scale circles event direction))))

(defn undo [events circles]
  (event/commit-last-scale events)
  (handle-historic-scale circles events event/undo :from))

(defn redo [events circles]
  (handle-historic-scale circles events event/redo :to))
