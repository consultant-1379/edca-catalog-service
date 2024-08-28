{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "eric-edca-catalog.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create chart version as used by the chart label.
*/}}
{{- define "eric-edca-catalog.version" -}}
{{- printf "%s" .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "eric-edca-catalog.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- $name | trunc 63 | trimSuffix "-" -}}
{{/* Ericsson mandates the name defined in metadata should start with chart name. */}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "eric-edca-catalog.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create catalog image registry url
*/}}
{{- define "eric-edca-catalog.registryUrl" -}}
  {{- $registryUrl := "armdocker.rnd.ericsson.se" -}}
  {{- if .Values.global -}}
    {{- if .Values.global.registry -}}
      {{- if .Values.global.registry.url -}}
        {{- $registryUrl = .Values.global.registry.url -}}
      {{- end -}}
    {{- end -}}
  {{- end -}}
  {{- if .Values.imageCredentials.registry -}}
    {{- if .Values.imageCredentials.registry.url -}}
    {{- $registryUrl = .Values.imageCredentials.registry.url -}}
    {{- end -}}
  {{- end -}}
  {{- print $registryUrl -}}
{{- end -}}

{{/*
Create image pull secret, service level parameter takes precedence
*/}}
{{- define "eric-edca-catalog.pullSecret" -}}
{{- $pullSecret := "" -}}
{{- if .Values.global -}}
    {{- if .Values.global.pullSecret -}}
        {{- $pullSecret = .Values.global.pullSecret -}}
    {{- end -}}
{{- end -}}
{{- if .Values.imageCredentials -}}
    {{- if .Values.imageCredentials.pullSecret -}}
        {{- $pullSecret = .Values.imageCredentials.pullSecret -}}
    {{- end -}}
{{- end -}}
{{- print $pullSecret -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "eric-edca-catalog.labels" -}}
app.kubernetes.io/name: {{ include "eric-edca-catalog.name" . }}
helm.sh/chart: {{ include "eric-edca-catalog.chart" . }}
app.kubernetes.io/version: {{ include "eric-edca-catalog.version" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app: {{ include "eric-edca-catalog.name" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{- define "eric-edca-catalog.product-info" -}}
ericsson.com/product-name: "Data Catalog"
ericsson.com/product-number: "CXD 174 1531"
ericsson.com/product-revision: "1.0.0"
{{- end -}}

{{/*
 create prometheus info
*/}}
{{- define "eric-edca-catalog.prometheus" -}}
prometheus.io/path: "/catalog/actuator/prometheus"
prometheus.io/port: "{{ .Values.service.port }}"
prometheus.io/scrape: "true"
{{- end -}}

{{/*
Create a user defined annotation (DR-D1121-068)
*/}}
{{- define "eric-edca-catalog.user-labels" }}
{{- if .Values.labels }}
{{ toYaml .Values.labels }}
{{- end }}
{{- end }}