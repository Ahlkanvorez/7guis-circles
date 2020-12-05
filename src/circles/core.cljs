(ns circles.core
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [circles.canvas :as canvas]
            [circles.db :as db]
            [circles.events.input :as input]
            [circles.history :as history]
            [circles.views :as views]))

(defn update-via [update-history canvas-id events circles]
  (update-history events circles)
  (db/rebuild circles events)
  (canvas/draw (js/document.getElementById canvas-id) @circles))

(defn circle-drawer [{:keys [events circles]}]
  (let [canvas-id "circles-canvas"
        width 300]
    [views/rows width
     [views/history-buttons
      {:on-undo #(update-via history/undo canvas-id events circles)
       :on-redo #(update-via history/redo canvas-id events circles)
       :can-undo? (not (empty? (:events @events)))
       :can-redo? (not (empty? (:redo @events)))}]
     [views/canvas {:id canvas-id
                    :width width
                    :height width ;; height = width -> square canvas
                    :on-click (input/click-handler canvas-id events circles)}]
     (when-let [circle (db/selected @circles)]
       [views/circle-scaler
        {:circle circle
         :on-scale (input/scale-handler canvas-id events circles circle)}])]))

(defn initial-state []
  {:circles (r/atom {:all-circles {} :circles {} :selected nil})
   :events (r/atom {:events () :redo () :last-scale nil})}              )

(defn mount-root []
  (rd/render [circle-drawer (initial-state)]
             (js/document.getElementById "app-root")))
