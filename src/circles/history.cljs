(ns circles.history
  (:require [circles.db :as db]
            [circles.event :as event]))

(defn handle-historic-scale
  "`traverse` `events` in `direction`, and update `db` accordingly.
  This resets circle sizes based on the scale events in the new event
  store."
  [db events traverse direction]
  (let [event (traverse events)]
    (when (= :scale (:event event))
      (db/apply-scale db event direction))))

(defn undo [events db]
  (event/commit-last-scale events)
  (handle-historic-scale db events event/undo :from))

(defn redo [events db]
  (handle-historic-scale db events event/redo :to))
