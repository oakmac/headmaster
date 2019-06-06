(ns headmaster.ui.db
  (:require
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

(def initial-app-db
  {:class nil
   :page "DASHBOARD_PAGE"})
