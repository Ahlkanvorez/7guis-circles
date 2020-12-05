(ns circles.event)

(defn next-id
  "Return an event id larger than any in `events`.
  This id is guaranteed to be unique compared to all ids in `events`
  when invoked."
  [events]
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

(defn undo
  "Move the most recent event to the redo stack; return that event."
  [events]
  (move events :events :redo))

(defn redo
  "Move the most recently undone event to the redo stack; return that
  event."
  [events]
  (move events :redo :events))

(defn add
  "Register a new event in the event store; return the redo stack.
  This clears all redoable events, since by adding a new event the
  user is making a new path in the history. The removed events are
  returned for any necessary cleanup or processing."
  [events event]
  (add-to events event :events)
  (clear-redo events))

(defn commit-last-scale
  "Save the last scale as an event in the store.
  The requirement that scale events be committed prevents the event
  store from being flooded with scale events resulting from sliding
  the range input. This lets users undo or redo to the expected circle
  instead of having to click undo or redo numerous times to gradually
  shrink or grow the circle.
  A scale event should only be committed when the user performs some
  other action indicating the scale is complete; e.g.
  - Selecting another circle
  - Creating a new circle
  - Clicking undo or redo"
  [events]
  (when-let [scale (:last-scale @events)]
    (add events (assoc scale :id (next-id @events)))
    (swap! events assoc :last-scale nil)))
