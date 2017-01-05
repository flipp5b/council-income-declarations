(ns council-income-declarations.datasource
  (:require [net.cgrand.enlive-html :as enlive]
            [council-income-declarations.income-declaration :as income-declaration])
  (:import (java.net URL)))

(declare get-names&urls get-name get-url)

(defn scrape [url]
  (let [names&urls (-> url
                       URL.
                       enlive/html-resource
                       get-names&urls)]
    (map
      #(assoc (income-declaration/scrape (:url %)) :name (:name %))
      names&urls)))

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