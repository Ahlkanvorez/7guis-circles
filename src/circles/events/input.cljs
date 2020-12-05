(ns circles.events.input
  (:require [circles.canvas :as scene]
            [circles.circle :as circle]
            [circles.db :as db]
            [circles.event :as event]
            [circles.events.click :as click]
            [circles.events.scale :as scale]))

(defn click-handler [canvas-id events circles]
  (fn [event]
    (let [canvas (js/document.getElementById canvas-id)
          event (click/make event canvas @events)]
      (event/commit-last-scale events)
      (if-let [clicked (scene/selected-circle (vals (:circles @circles))
                                              event)]
        (swap! circles assoc :selected (:id clicked))
        (let [removed (event/add events event)]
          (doseq [event removed]
            (swap! circles update :all-circles dissoc (:id event)))
          (swap! circles assoc-in [:all-circles (:id event)]
                 (circle/event->circle event))
          (db/rebuild circles events)))
      (scene/draw canvas @circles))))

(defn scale-handler [canvas-id events circles selected-circle]
  (fn [event]
    (let [old-radius (get-in @circles
                             [:all-circles (:id selected-circle) :radius])
          new-radius (/ (int (.. event -target -value)) 2)
          last-scale (:last-scale @events)
          new-scale (scale/make (:id selected-circle)
                                (get last-scale :from old-radius)
                                new-radius)
          scale (swap! events assoc :last-scale new-scale)]
      (db/apply-scale circles new-scale :to)
      (db/rebuild circles events)
      (scene/draw (js/document.getElementById canvas-id) @circles))))
