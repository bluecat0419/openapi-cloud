import Layout from "@/layout/index.vue"
import ParentView from "@/components/ParentView/index.vue"
import InnerLink from "@/layout/components/InnerLink/index.vue"

function addToString(obj, explainName) {
  obj.toString = function () {
    return explainName
  }
}

addToString(Layout, "Layout")
addToString(ParentView, "ParentView")
addToString(InnerLink, "InnerLink")

export { Layout, ParentView, InnerLink }
