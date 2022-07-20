/*
*
*  Copyright 2012-2022 Aerospike, Inc.
*
*  Portions may be licensed to Aerospike, Inc. under one or more contributor
*  license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not
*  use this file except in compliance with the License. You may obtain a copy of
*  the License at http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
*  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
*  License for the specific language governing permissions and limitations under
*  the License.
*/

dependencies {
    // JSR 305 for annotations
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    // Aerospike Java Client
    compileOnly("com.aerospike:aerospike-client:5.1.11")
}