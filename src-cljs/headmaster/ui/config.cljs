(ns headmaster.ui.config
  (:import goog.Uri)
  (:require
    [clojure.string :as str]
    [oops.core :refer [oget]]))

(def on-development-page? (str/includes? (oget js/window "location.href") "development.html"))

(defn- query-param-exists? [param]
  (-> (Uri. (oget js/window "location"))
      (.getQueryData)
      (.containsKey param)))

;; FIXME: toggle these manually during development; switch to query-based later
(def config
  {:data-mode "MOCK_DATA"})

(defn in-mock-mode? []
  (= "MOCK_DATA" (:data-mode config)))
