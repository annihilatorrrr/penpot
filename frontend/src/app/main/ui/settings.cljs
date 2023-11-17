;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.settings
  (:require-macros [app.main.style :as stl])
  (:require
   [app.main.refs :as refs]
   [app.main.store :as st]
   [app.main.ui.context :as ctx]
   [app.main.ui.settings.access-tokens :refer [access-tokens-page]]
   [app.main.ui.settings.change-email]
   [app.main.ui.settings.delete-account]
   [app.main.ui.settings.feedback :refer [feedback-page]]
   [app.main.ui.settings.options :refer [options-page]]
   [app.main.ui.settings.password :refer [password-page]]
   [app.main.ui.settings.profile :refer [profile-page]]
   [app.main.ui.settings.sidebar :refer [sidebar]]
   [app.util.dom :as dom]
   [app.util.i18n :as i18n :refer [tr]]
   [app.util.router :as rt]
   [rumext.v2 :as mf]))

(mf/defc header
  {::mf/wrap [mf/memo]}
  []
  (let [new-css-system (mf/use-ctx ctx/new-css-system)]
    (if new-css-system
      [:header {:class (stl/css :dashboard-header)}
       [:div {:class (stl/css :dashboard-title)}
        [:h1 {:data-test "account-title"} (tr "dashboard.your-account-title")]]]

      ;; OLD
      [:header.dashboard-header
       [:div.dashboard-title
        [:h1 {:data-test "account-title"} (tr "dashboard.your-account-title")]]])))

(mf/defc settings
  [{:keys [route] :as props}]
  (let [new-css-system (mf/use-ctx ctx/new-css-system)
        section (get-in route [:data :name])
        profile (mf/deref refs/profile)
        locale  (mf/deref i18n/locale)]

    (mf/use-effect
     #(when (nil? profile)
        (st/emit! (rt/nav :auth-login))))

    (if new-css-system
      [:section {:class (stl/css :dashboard-layout-refactor :dashboard)}
       [:& sidebar {:profile profile
                    :locale locale
                    :section section}]

       [:div {:class (stl/css :dashboard-content)}
        [:& header]
        [:section {:class (stl/css :dashboard-container)}
         (case section
           :settings-profile
           [:& profile-page {:locale locale}]

           :settings-feedback
           [:& feedback-page]

           :settings-password
           [:& password-page {:locale locale}]

           :settings-options
           [:& options-page {:locale locale}]

           :settings-access-tokens
           [:& access-tokens-page])]]]

      ;; OLD
      [:section {:class (dom/classnames :dashboard-layout (not new-css-system)
                                        :dashboard-layout-refactor new-css-system)}
       [:& sidebar {:profile profile
                    :locale locale
                    :section section}]

       [:div.dashboard-content
        [:& header]
        [:section.dashboard-container
         (case section
           :settings-profile
           [:& profile-page {:locale locale}]

           :settings-feedback
           [:& feedback-page]

           :settings-password
           [:& password-page {:locale locale}]

           :settings-options
           [:& options-page {:locale locale}]

           :settings-access-tokens
           [:& access-tokens-page])]]])))
