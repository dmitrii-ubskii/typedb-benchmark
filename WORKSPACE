#
# Copyright (C) 2022 Vaticle
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

workspace(name = "vaticle_typedb_benchmark")

################################
# Load @vaticle_dependencies #
################################

load("//dependencies/vaticle:repositories.bzl", "vaticle_dependencies")
vaticle_dependencies()

# Load //builder/bazel for RBE
load("@vaticle_dependencies//builder/bazel:deps.bzl", "bazel_toolchain")
bazel_toolchain()

# Load //builder/java
load("@vaticle_dependencies//builder/java:deps.bzl", java_deps = "deps")
java_deps()

# Load //builder/kotlin
load("@vaticle_dependencies//builder/kotlin:deps.bzl", kotlin_deps = "deps")
kotlin_deps()
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")
kotlin_repositories()
kt_register_toolchains()

# Load //builder/python
load("@vaticle_dependencies//builder/python:deps.bzl", python_deps = "deps")
python_deps()

# Load //builder/antlr
load("@vaticle_dependencies//builder/antlr:deps.bzl", antlr_deps = "deps", "antlr_version")
antlr_deps()

load("@rules_antlr//antlr:lang.bzl", "JAVA")
load("@rules_antlr//antlr:repositories.bzl", "rules_antlr_dependencies")
rules_antlr_dependencies(antlr_version, JAVA)

# Load //builder/grpc
load("@vaticle_dependencies//builder/grpc:deps.bzl", grpc_deps = "deps")
grpc_deps()
load("@com_github_grpc_grpc//bazel:grpc_deps.bzl",
com_github_grpc_grpc_deps = "grpc_deps")
com_github_grpc_grpc_deps()
load("@stackb_rules_proto//java:deps.bzl", "java_grpc_compile")
java_grpc_compile()

# Load //tool/common
load("@vaticle_dependencies//tool/common:deps.bzl", "vaticle_dependencies_ci_pip",
    vaticle_dependencies_tool_maven_artifacts = "maven_artifacts")
vaticle_dependencies_ci_pip()

# Load //tool/checkstyle
load("@vaticle_dependencies//tool/checkstyle:deps.bzl", checkstyle_deps = "deps")
checkstyle_deps()

# Load //tool/unuseddeps
load("@vaticle_dependencies//tool/unuseddeps:deps.bzl", unuseddeps_deps = "deps")
unuseddeps_deps()

# Load //tool/sonarcloud
load("@vaticle_dependencies//tool/sonarcloud:deps.bzl", "sonarcloud_dependencies")
sonarcloud_dependencies()

######################################
# Load @vaticle_bazel_distribution #
######################################

load("@vaticle_dependencies//distribution:deps.bzl", "vaticle_bazel_distribution")
vaticle_bazel_distribution()

# Load //common
load("@vaticle_bazel_distribution//common:deps.bzl", "rules_pkg")
rules_pkg()
load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")
rules_pkg_dependencies()

# Load //github
load("@vaticle_bazel_distribution//github:deps.bzl", github_deps = "deps")
github_deps()

# Load //pip
load("@vaticle_bazel_distribution//pip:deps.bzl", pip_deps = "deps")
pip_deps()

######################################
# Load groovy dependencies #
######################################

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
http_archive(
    name = "io_bazel_rules_groovy",
    url = "https://github.com/bazelbuild/rules_groovy/archive/0.0.6.tar.gz",
    sha256 = "21c7172786623f280402d3b3a2fc92f36568afad5a4f6f5ea38fd1c6897aecf8",
    strip_prefix = "rules_groovy-0.0.6",
)
#Rather than use these recommended lines, we use the below such that we have access to the groovy library in our groovy scripts
#load("@io_bazel_rules_groovy//groovy:repositories.bzl", "rules_groovy_dependencies")
#rules_groovy_dependencies()

http_archive(
    name = "groovy_sdk_artifact",
    urls = [
        "https://groovy.jfrog.io/artifactory/dist-release-local/groovy-zips/apache-groovy-binary-3.0.6.zip"
    ],
    build_file_content = """
filegroup(
    name = "sdk",
    srcs = glob(["groovy-3.0.6/**"]),
    visibility = ["//visibility:public"],
)
java_import(
    name = "groovy",
    jars = ["groovy-3.0.6/lib/groovy-3.0.6.jar", "groovy-3.0.6/lib/groovy-templates-3.0.6.jar"],
    visibility = ["//visibility:public"],
)
java_import
"""
)

bind(
    name = "groovy-sdk",
    actual = "@groovy_sdk_artifact//:sdk",
)
bind(
    name = "groovy",
    actual = "@groovy_sdk_artifact//:groovy",
)

################################
# Load @vaticle dependencies #
################################

# Load repositories
load("//dependencies/vaticle:repositories.bzl", "vaticle_factory_tracing", "vaticle_typedb_client_java")
vaticle_factory_tracing()
vaticle_typedb_client_java()
load("@vaticle_typedb_client_java//dependencies/vaticle:repositories.bzl", "vaticle_typedb_common", "vaticle_typeql", "vaticle_typedb_protocol")
vaticle_typedb_common()
vaticle_typeql()
vaticle_typedb_protocol()

# Load artifact
load("//dependencies/vaticle:artifacts.bzl", "vaticle_typedb_artifacts", "vaticle_typedb_cluster_artifacts")
vaticle_typedb_artifacts()
vaticle_typedb_cluster_artifacts()

# Load maven
load("//dependencies/maven:artifacts.bzl", vaticle_typedb_benchmark_artifacts = "artifacts")
load("@vaticle_typedb_client_java//dependencies/maven:artifacts.bzl", vaticle_typedb_client_java_artifacts = "artifacts")
load("@vaticle_factory_tracing//dependencies/maven:artifacts.bzl", vaticle_factory_tracing_artifacts = "artifacts")
load("@vaticle_typedb_protocol//dependencies/maven:artifacts.bzl", vaticle_typedb_protocol_artifacts = "artifacts")
load("@vaticle_typeql//dependencies/maven:artifacts.bzl", vaticle_typeql_artifacts = "artifacts")

# Load neo4j
load("@rules_jvm_external//:defs.bzl", "maven_install")
maven_install(
    name = "neo4j",
    artifacts = ["org.neo4j.driver:neo4j-java-driver:4.4.5"],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
    strict_visibility = True,
    version_conflict_policy = "pinned"
)

############################
# Load @maven dependencies #
############################

load("@vaticle_dependencies//library/maven:rules.bzl", "maven")
maven(
    vaticle_typedb_benchmark_artifacts +
    vaticle_typeql_artifacts +
    vaticle_typedb_protocol_artifacts +
    vaticle_typedb_client_java_artifacts +
    vaticle_factory_tracing_artifacts
)
