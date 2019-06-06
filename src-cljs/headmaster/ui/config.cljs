(ns headmaster.ui.config
  (:import goog.Uri)
  (:require
    [oops.core :refer [oget]]))

(defn- query-param-exists? [param]
  (-> (Uri. (oget js/window "location"))
      (.getQueryData)
      (.containsKey param)))

;; FIXME: toggle these manually during development; switch to query-based later
(def config
  {:data-mode "MOCK_DATA"})

(defn in-mock-mode? []
  (= "MOCK_DATA" (:data-mode config)))
