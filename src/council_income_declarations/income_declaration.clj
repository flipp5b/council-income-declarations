(ns council-income-declarations.income-declaration
  (:require [clojure.java.io :as io])
  (:import (org.apache.poi.hwpf.extractor WordExtractor)))

(declare income-declaration sting->number)

(defn scrape [url]
  (with-open [is (io/input-stream url)]
    (-> is
        WordExtractor.
        .getText
        income-declaration)))

(defn- income-declaration [text]
  {:post          (re-find #"(?<=Замещаемая должность\t).+" text)
   :income        (-> (re-find #"1\tДекларированный годовой доход.+\t(.+)" text)
                      (get 1)
                      sting->number)
   :spouse-income (sting->number (re-find #"(?<=\tСупруг.\t).+" text))
   :raw           text})

(defn- sting->number [s]
  (if s (bigdec s)))