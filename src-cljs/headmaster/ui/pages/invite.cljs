(ns headmaster.ui.pages.invite
  (:require
    [headmaster.ui.html.common :refer [ClassHeader PrimaryNav]]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]))

;;------------------------------------------------------------------------------
;; Subscriptions

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :invite/init
  (fn [db _]
    (assoc db :page "INVITE_PAGE")))

;;------------------------------------------------------------------------------
;; Views

(defn InvitePage
  []
  [:section.section
    [ClassHeader]
    [PrimaryNav]
    [:div.content
      [:h2 "HERE IS WHERE I'D SEND AN INVITE"]]])
