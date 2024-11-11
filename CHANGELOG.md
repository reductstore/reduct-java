# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed:

- Resolve bug: Bucket.query handle only one HTTP GET request. The query now returns the records until gets last
  bucket [PR-50](https://github.com/reductstore/reduct-java/issues/47)

## 0.1.0 - 2024-08-21

### Added:

- BucketClient.createBucket() method to create a bucket [PR-10](https://github.com/reductstore/reduct-java/pull/10)
- TokenClient.createToken() method to create a token [PR-4](https://github.com/reductstore/reduct-java/pull/4)
- ServerClient.serverInfo() method to get server info [PR-3](https://github.com/reductstore/reduct-java/pull/3)
- ServerClient.getList() method to get a list of the buckets with their
  stats [PR-16](https://github.com/reductstore/reduct-java/pull/16)
- ServerClient.isAlive() method to check if the storage engine is
  working [PR-17](https://github.com/reductstore/reduct-java/pull/17)
- TokenClient.getTokens() method to get a list of tokens [PR-20](https://github.com/reductstore/reduct-java/pull/20)
- EntryClient.writeRecord(Bucket bucket, Entry<?> body) method to add a record to a
  bucket [PR-25](https://github.com/reductstore/reduct-java/issues/25)
- EntryClient.getRecord(Bucket bucket, Entry<?> body) method to get a record from a
  bucket [PR-27](https://github.com/reductstore/reduct-java/issues/27)
- Improve ReductClient initialization. [PR-41](https://github.com/reductstore/reduct-java/issues/41)
- Add exists option to BucketSettings. [PR-42](https://github.com/reductstore/reduct-java/issues/42)
- Bucket.writeRecord receives entry name and bucket. [PR-43](https://github.com/reductstore/reduct-java/issues/43)
### Infrastructure:

- Added GitHub Actions for CI/CD [PR-35](https://github.com/reductstore/reduct-java/pull/35)
