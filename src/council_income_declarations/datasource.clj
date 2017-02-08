(ns council-income-declarations.datasource
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.java.io :as io]
            [council-income-declarations.income-declaration :as decl])
  (:import (java.util.concurrent ConcurrentLinkedQueue)))

(declare get-names&urls get-name get-url read-raw-decl parse-decl)

(def ^:dynamic *number-of-workers* 5)

(defn scrape [url]
  (let [names&urls (-> url
                       io/as-url
                       enlive/html-resource
                       get-names&urls
                       ConcurrentLinkedQueue.)
        final-size (count names&urls)
        result (atom [])
        agents (repeatedly *number-of-workers* #(agent {:names&urls names&urls :result result}))
        result-promise (promise)]
    (add-watch result :finish (fn [_ _ _ new] (when (= (count new) final-size) (deliver result-promise new))))
    (doseq [a agents] (send-off a read-raw-decl))
    result-promise))

(defn- get-names&urls [html]
  (map
    #(hash-map :name (get-name %), :url (get-url %))
    (enlive/select html [:div#sf :div.council_block])))

(defn- get-name [council-div]
  (-> council-div
      (enlive/select [:.council_title])
      first
      enlive/text))

(defn- get-url [council-div]
  (-> council-div
      (enlive/select [:a])
      first
      :attrs
      :href))

(defn- read-raw-decl [{:keys [names&urls result] :as state}]
  (when-let [{:keys [name url]} (.poll names&urls)]
    (send *agent* parse-decl)
    (assoc state :text (decl/read-raw url) :name name)))

(defn- parse-decl [{:keys [names&urls result text name]}]
  (swap! result conj (assoc (decl/parse text) :name name))
  (send-off *agent* read-raw-decl)
  {:names&urls names&urls :result result})