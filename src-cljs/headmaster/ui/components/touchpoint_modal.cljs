(ns headmaster.ui.components.touchpoint-modal
  (:require
    [headmaster.ui.html.common :as common]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Subscriptions

(rf/reg-sub
  :touchpoint-modal
  (fn [db _]
    (:touchpoint-modal db)))

(rf/reg-sub
  :touchpoint-modal/student
  (fn [db]
    (let [student-id (get-in db [:touchpoint-modal :student-id])]
      (get-in db [:class :students (keyword student-id)]))))

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :touchpoint-modal/open
  (fn [db [_ student-id]]
    (assoc db :touchpoint-modal {:student-id student-id})))

(rf/reg-event-db
  :touchpoint-modal/close
  (fn [db _]
    (assoc db :touchpoint-modal nil)))

;;------------------------------------------------------------------------------
;; Views

(defn- close-modal [_js-evt]
  (rf/dispatch [:touchpoint-modal/close]))

(defn TouchpointModal []
  (let [touchpoint-modal @(rf/subscribe [:touchpoint-modal])
        {:keys [name] :as student} @(rf/subscribe [:touchpoint-modal/student])]
    [:div.modal.is-active
      [:div.modal-background]
      [:div.modal-card
        [:header.modal-card-head
          [:p.modal-card-title "Add Touchpoint for " name]
          [:button.delete {:on-click close-modal}]]
        [:section.modal-card-body
          [:h1 "TODO: add input elements for touchpoint here"]]
        [:footer.modal-card-foot
          [:button.button.is-success "Add Touchpoint"]
          [:button.button {:on-click close-modal}"Cancel"]]]]))
