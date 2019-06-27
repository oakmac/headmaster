(ns headmaster.ui.config
  (:import goog.Uri)
  (:require
    [clojure.string :as str]
    [oops.core :refer [oget]]))

;; TODO: add the ability to override this via query param or localStorage
(def on-development-page? (str/includes? (oget js/window "location.href") "development.html"))

(defn- query-param-exists? [param]
  (-> (Uri. (oget js/window "location"))
      (.getQueryData)
      (.containsKey param)))

(def config
  {:data-mode (if (query-param-exists? "mock-mode") "MOCK_DATA" "API_DATA")
   :hide-student-status? (query-param-exists? "hide-status")})

(defn in-mock-mode? []
  (= "MOCK_DATA" (:data-mode config)))

(defn hide-student-status? []
  (true? (:hide-student-status? config)))
