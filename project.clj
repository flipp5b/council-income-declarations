(defproject council-income-declarations "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.poi/poi-scratchpad "3.15"]
                 [enlive/enlive "1.1.6"]]
  :main ^:skip-aot council-income-declarations.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
