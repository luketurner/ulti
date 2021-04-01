(ns ulti.icons
  (:require [reagent.core :as reagent]))

(def default-spritesheet (reagent/atom ""))
(def default-icon-prefix (reagent/atom ""))

(defn set-default-spritesheet! [x] (reset! default-spritesheet x))
(defn set-default-icon-prefix! [x] (reset! default-icon-prefix x))

(defn css-rules [theme]
  [[:.icon {:display :inline-block :width "1em" :height "1em" :stroke-width 0 :stroke :currentColor :fill :currentColor}]])

(defn icon [{:keys [icon spritesheet icon-prefix]}]
  [:svg.icon {:xmlns "http://www.w3.org/2000/svg"}
   [:use {:href (str (if (some? spritesheet) spritesheet @default-spritesheet)
                     "#"
                     (if (some? icon-prefix) icon-prefix @default-icon-prefix)
                     (name icon))}]])

