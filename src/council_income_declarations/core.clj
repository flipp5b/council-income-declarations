(ns council-income-declarations.core
  (:require [council-income-declarations.datasource :as ds]))

(defn analyze [year]
  (let [url (str "http://council.gov.ru/structure/machinery/vacancies/property/?year=" year)
        result @(ds/scrape url)]
    (println "Done! Result size:" (count result))))