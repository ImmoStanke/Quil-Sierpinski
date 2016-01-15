(defproject sierpinski "0.1.0-SNAPSHOT"
  :description "A zooming sierpinsky triangle in quil"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [quil "2.3.0"]]
  :aot [sierpinski.triangle-builder, sierpinski.core]
  :main sierpinski.core
)
