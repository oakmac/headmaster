(ns headmaster.ui.html.layout
  "TODO: remove this namespace"
  (:require
    [headmaster.ui.html.common :refer [PrimaryNav]]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]))

(defn ClassPageWrapper
  [body-component app-state]
  (when (:class app-state)
    [:section.section
      [:h1.title (get-in app-state [:class :title])]
      [:h2.subtitle (get-in app-state [:class :subtitle])]
      [PrimaryNav (:page app-state)]
      [body-component]]))
