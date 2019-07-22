(ns headmaster.backend.express.middleware
  (:require
    [clojure.core.async :refer [chan put!]]
    [headmaster.backend.data :as data]
    [oops.core :refer [oget]]
    [taoensso.timbre :as timbre]))

(defn foo [req res next-fn]
  (timbre/info "middleware ns!")
  (next-fn))
