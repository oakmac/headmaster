(ns headmaster.ui.pages.manage-cohort
  (:require
    [headmaster.ui.components.common :as common]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Subscriptions

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :manage-page/init
  (fn [db _]

    (timbre/info (:cohort db))
    (timbre/info "4444444444444444444444444444444444444444444")

    (assoc db :page "MANAGE_COHORT_PAGE"
              :manage-page {})))

(rf/reg-event-db
  :manage-page/submit-form
  (fn [db _]
    (assoc db :page "MANAGE_COHORT_PAGE")))


;;------------------------------------------------------------------------------
;; Views

(defn TitleInput []
  (let [title ""]
    [:div.field
      [:label.label "Title"]
      [:div.control
        [:input.input {:type "text" :placeholder "Cohort Title" :value title}]]]))

(defn SubtitleInput []
  (let [subtitle ""]
    [:div.field
      [:label.label "Subtitle"]
      [:div.control
        [:input.input {:type "text" :placeholder "Cohort Subtitle" :value subtitle}]]]))

(defn ActionBar []
  [:div.field.is-grouped
    [:div.control
      [:button.button.is-primary
        {:on-click #(rf/dispatch [:manage-page/submit-form])}
        "Save"]]])

(defn InfoForm []
  [:form
    [TitleInput]
    [SubtitleInput]
    [ActionBar]])

(defn ManageCohortPage
  []
  [:section.section
    [common/CohortHeader]
    [common/PrimaryNav]
    [:div.content
      [:h2 "TODO: Manage Page"]
      [:hr]
      [:ul
        [:li "Add / Edit / Remove student"]
        [:li "Start date for the Github graph"]
        [:li "Edit title / subtitle"]
        [:li "Create invite link"]
        [:li "Add / Remove authorized users"]]
      [:hr]
      [InfoForm]]])



; <div class="field">
;   <label class="label">Name</label>
;   <div class="control">
;     <input class="input" type="text" placeholder="Text input">
;   </div>
; </div>
;
; <div class="field">
;   <label class="label">Username</label>
;   <div class="control has-icons-left has-icons-right">
;     <input class="input is-success" type="text" placeholder="Text input" value="bulma">
;     <span class="icon is-small is-left">
;       <i class="fas fa-user"></i>
;     </span>
;     <span class="icon is-small is-right">
;       <i class="fas fa-check"></i>
;     </span>
;   </div>
;   <p class="help is-success">This username is available</p>
; </div>
;
; <div class="field">
;   <label class="label">Email</label>
;   <div class="control has-icons-left has-icons-right">
;     <input class="input is-danger" type="email" placeholder="Email input" value="hello@">
;     <span class="icon is-small is-left">
;       <i class="fas fa-envelope"></i>
;     </span>
;     <span class="icon is-small is-right">
;       <i class="fas fa-exclamation-triangle"></i>
;     </span>
;   </div>
;   <p class="help is-danger">This email is invalid</p>
; </div>
