(ns headmaster.ui.actions.students-page
  "Actions for the Students Page"
  (:require
    [headmaster.ui.util :as util]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))


; (defn sort-by-name []
;   (let [sort-info (get-in @*app-state [:students-page])
;         {:keys [sort-col sort-dir]} sort-info]
;     (if-not (= sort-col :name)
;       (swap! *app-state update-in [:students-page] merge {:sort-col :name
;                                                           :sort-dir :asc})
;       (swap! *app-state update-in [:students-page :sort-dir]
;              (fn [dir]
;                (if (= dir :asc)
;                  :desc
;                  :asc))))))
;
;
; (defn sort-by-stoplight []
;   (let [sort-info (get-in @*app-state [:students-page])
;         {:keys [sort-col sort-dir]} sort-info]
;     (if-not (= sort-col :stoplight)
;       (swap! *app-state update-in [:students-page] merge {:sort-col :stoplight
;                                                           :sort-dir :asc})
;       (swap! *app-state update-in [:students-page :sort-dir]
;              (fn [dir]
;                (if (= dir :asc)
;                  :desc
;                  :asc))))))
;
;
; (defn- initial-page-state []
;   {:sort-col :name
;    :sort-dir :asc})
;
;
; (defn init-student-page! []
;   (js-log "init student page!!!!!"))
;
;
; (defn init! []
;   (swap! *app-state assoc :page "STUDENTS_PAGE"
;                           :students-page (initial-page-state)))
