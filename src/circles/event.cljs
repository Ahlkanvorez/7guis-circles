(ns circles.event)

(defn next-id [events]
  (->> (concat [{:id 0}] (:events events) (:redo events))
       (map :id)
       (apply max)
       inc))

(defn clear-redo [events]
  (let [removed (:redo @events)]
    (swap! events assoc :redo ())
    removed))

(defn add-to [events event stack]
  (swap! events update stack conj event))

(defn move [events source dest]
  (when-let [event (first (get @events source ()))]
    (swap! events update source rest)
    (add-to events event dest)
    event))

(defn undo [events]
  (move events :events :redo))

(defn redo [events]
  (move events :redo :events))

(defn add [events event]
  (add-to events event :events)
  (clear-redo events))

(defn commit-last-scale [events]
  (when-let [scale (:last-scale @events)]
    (add events (assoc scale :id (next-id @events)))
    (swap! events assoc :last-scale nil)))
