(ns lt-ui.docs.inputs
  (:require [lt-ui.devcards :refer-macros [defcard']]
            [lt-ui.inputs :as inputs]
            [clojure.string :as string]))

(defcard' basic-input-usage
  "The basic text input."
  (fn [data-atom _]
    [inputs/text (inputs/cursor @data-atom #(reset! data-atom %) [:value])])
  {:value nil :error nil}
  {:inspect-data true})

(defcard' validating-inputs
  "Inputs can be validated by specifying a `validator` function.
   
   If the validator returns false for a value, `on-change` will not be called.
   Instead, `on-error` will be called with an error message (which is generated by
   calling `show-error` on the invalid value.) If the value later becomes valid, `on-error`
   will be called again with a value `nil`.
   
   Invalid input is maintained as transient state within the input component. If the `value` is
   updated externally (e.g. by some other event in the system), the invalid content will
   be replaced with the new `value`.

   Invalid inputs are highlighted in red automatically.
   
   In this example, text is only valid when it contains the character `q`."
  (fn [data-atom _]
    [inputs/text (inputs/cursor @data-atom #(reset! data-atom %) [:value]
                                {:validator #(string/includes? % "q")
                                 :show-error #(str "Must include a 'q'")
                                 :on-error #(swap! data-atom assoc :error %)})])
  {:value nil :error nil}
  {:inspect-data true})