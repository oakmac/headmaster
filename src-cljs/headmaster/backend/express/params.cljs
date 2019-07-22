(ns headmaster.backend.express.params
  (:require
    [clojure.core.async :refer [chan put!]]
    [clojure.string :as str]
    [headmaster.backend.data :as data]
    [headmaster.backend.html :as html]
    [oops.core :refer [oget oset!]]
    [taoensso.timbre :as timbre]))

;; TODO: move these to a generic namespace
(defn- api-not-found [res]
  (.status res 404)
  (.json res (js-obj {"msg" "not found"})))

(defn- not-found-page [res]
  (.status res 404)
  (.send res (html/not-found-page)))

(defn- api-url?
  "Is this path for an API request?"
  [path]
  (str/includes? path "/api/"))

(defn cohort-slug
  "adds headmaster.cohort the request object if the Cohort slug was found
   404s otherwise"
  [req res next-fn]
  (let [api? (api-url? (oget req "path"))
        slug (oget req "params.?cohortSlug")]
    (data/get-cohort slug
      (fn [cohort]
        (cond
          (and api? (not cohort)) (api-not-found res)
          (not cohort) (not-found-page res)
          :else
          (do
            (oset! req "!headmaster.!cohort" (clj->js cohort))
            (next-fn)))))))
