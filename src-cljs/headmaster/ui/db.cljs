(ns headmaster.ui.db
  (:require
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

(def initial-app-db
  {:cohort nil
   :page "STUDENTS_PAGE"})
