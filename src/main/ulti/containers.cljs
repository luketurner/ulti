(ns ulti.containers
  (:require [garden.selectors :as s]
            [garden.units :refer [vh vw rem px]]
            [reagent.core :as reagent]
            [ulti.util :refer-macros [defcomponent]]))

(defn css-rules [{:keys [line-height font-size color]}]
  (let [m-px (-> line-height (* font-size) (js/Math.floor))]
    [[:.application {:min-height (vh 100)
                     :width "100%"
                     :padding (px m-px)
                     :display "grid"
                     :grid-template-rows "fit-content(5ch) 1fr fit-content(5ch)"
                     :grid-template-columns "fit-content(20ch) minmax(min(50vw, 30ch), 1fr) fit-content(20ch)"
                     :grid-template-areas "\"header header header\" \"left   main   right\" \"footer footer footer\""
                     :gap (px m-px)
                     :color (get-in color [:chrome :fg])
                     :background-color (get-in color [:chrome :bg])}
      [(s/> :.sidebar-left) {:grid-area :left}]
      [(s/> :.sidebar-right) {:grid-area :right}]
      [(s/> :footer) {:grid-area :footer}]
      [(s/> :header) {:grid-area :header}]
      [(s/> :main) {:grid-area :main
                    :color (get-in color [:content :fg])
                    :background-color (get-in color [:content :bg])}]]
     [:.centered {:display :grid :place-content :center}]
     [:.vertical-split {:display :flex :flex-flow "row nowrap"}
      [(s/> :*) {:flex-grow 1}]]
     [:.horizontal-split {:display :flex :flex-flow "column nowrap"}
      [(s/> :*) {:flex-grow 1}]]
     [:.paper {:background-color (get-in color [:chrome :bg])
               :color (get-in color [:chrome :bg])
               :box-shadow ["0px 2px 1px -1px rgba(0, 0, 0, 0.2)" ; based on md box shadow
                            "0px 1px 1px 0px rgba(0, 0, 0, 0.14)"
                            "0px 1px 3px 0px rgba(0,0,0,.12)"]
               :padding (px m-px)
               :border-radius "5px"}
      [(s/> (s/last-child)) {:margin-bottom "0px"}]]
     [:.popover-container {:position :relative :z-index 1000}]
     [:.popover-handle {:cursor :pointer}]
     [:.popover-content {:position :absolute}]
     [:.utility-application-demo {:min-height (px 300)}]
     [:.utility-fill-color {:background-color "rgba(150, 200, 255, 0.5)" :width "100%" :height "100%"}]]))

(defcomponent application [{:keys [sidebar-left sidebar-right header footer children props]}]
  [:div.application props
   [:header header]
   [:div.sidebar-left sidebar-left]
   (into [:main] children)
   [:div.sidebar-right sidebar-right]
   [:footer footer]])

(defcomponent router [{:keys [views value]}]
  (get views value))

(defcomponent centered [{:keys [children props]}]
  (into [:div.centered props] children))

(defcomponent vertical-split [{:keys [children props]}]
  (into [:div.vertical-split props] children))

(defcomponent horizontal-split [{:keys [children props]}]
  (into [:div.horizontal-split props] children))

(defcomponent paper [{:keys [children props]}]
  (into [:div.paper props] children))

(defcomponent popover [{:keys [handle value on-change children props]}]
  (reagent/with-let [open? (reagent/atom false)
                     last-value (reagent/atom false)]
    (when (not= value @last-value) (reset! open? value) (reset! last-value value))
    [:span.popover-container props
     [:span.popover-handle {:on-click #(do 
                                        (swap! open? not)
                                        (when (fn? on-change) (on-change @open?)))}
      handle]
     (when @open?
       (into [:div.popover-content] children))]))