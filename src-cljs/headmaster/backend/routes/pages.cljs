(ns headmaster.backend.routes.pages
  (:require
    [clojure.core.async :refer [chan put!]]
    [headmaster.backend.data :as data]
    [headmaster.backend.html :as html]
    [oops.core :refer [oget]]
    [taoensso.timbre :as timbre]))

(defn homepage [_req res _next-fn]
  (.send res (html/homepage)))

(defn cohort [req res next-fn]
  (.send res "TODO: cohort page"))
