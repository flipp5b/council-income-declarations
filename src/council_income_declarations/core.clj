(ns council-income-declarations.core
  (:require [clojure.java.io :as io])
  (:import (org.apache.poi.hwpf.extractor WordExtractor)))

(defn fetch-income-declaration [url]
  (with-open [is (io/input-stream url)]
    (->
      is
      WordExtractor.
      .getText)))