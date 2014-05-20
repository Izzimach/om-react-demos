(defproject omreactdemos "0.1.0-SNAPSHOT"
  :description "It's a cookbook!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173" :scope "provided"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha" :scope "provided"]
                 [om "0.6.2"]
                 [ring/ring-core "1.2.2"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [com.facebook/react "0.9.0.1"]]

  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-ring "0.8.10"]]

  :source-paths ["src"]

  :ring {:handler webserver.core/statichandler :port 8081 }

  :cljsbuild {
    :builds [{:id "omreactdemos"
              :source-paths ["src/omreactdemos"]
              :compiler {
                :output-to "om-react-demos.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}
             {:id "packed"
              :source-paths ["src/omreactdemos"]
              :compiler {
                :output-to "om-react-demos.min.js"
                :output-dir "out-min"
                :optimizations :advanced
                :preamble ["react/react.min.js"]
                :externs ["react/react.js"]
                :closure-warnings {:externs-validation :off
                                    :non-standard-jsdoc :off}}}]})
