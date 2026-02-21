# AWS Resources — Root Account (887764105431)

Catalogued from AWS Resource Explorer on 2026-02-21. Cleanup performed same day.
Total after cleanup: ~66 resources (28 orphaned/stale entries removed).

## Managed by This Repo

| Resource            | ARN / Name                                              | Purpose                                                                           |
| ------------------- | ------------------------------------------------------- | --------------------------------------------------------------------------------- |
| CloudFormation      | `root-RootDnsStack`                                     | DNS alias records + delegation role                                               |
| CloudFormation      | `CDKToolkit`                                            | CDK bootstrap stack                                                               |
| Route53 hosted zone | `Z0315522208PWZSSBI9AL`                                 | `diyaccounting.co.uk` zone                                                        |
| Route53 domain      | `diyaccounting.co.uk`                                   | Domain registration                                                               |
| IAM role            | `root-github-actions-role`                              | OIDC auth for GitHub Actions                                                      |
| IAM role            | `root-deployment-role`                                  | CDK deploy role                                                                   |
| IAM role            | `root-route53-record-delegate`                          | Cross-account Route53 delegation                                                  |
| IAM OIDC provider   | `token.actions.githubusercontent.com`                   | GitHub Actions OIDC                                                               |
| Lambda              | `root-RootDnsStack-AWS679f53fac002430cb0da5b7982bd2-*`  | CDK custom resource handler                                                       |
| IAM role            | `root-RootDnsStack-AWS679f53fac002430cb0da5b7982bd22-*` | CDK custom resource execution role                                                |
| S3 bucket           | `cdk-hnb659fds-assets-887764105431-us-east-1`           | CDK asset staging                                                                 |
| ECR repository      | `cdk-hnb659fds-container-assets-*`                      | CDK container asset staging                                                       |
| SSM parameter       | `/cdk-bootstrap/hnb659fds/version`                      | CDK bootstrap version                                                             |
| IAM roles (6)       | `cdk-hnb659fds-*-887764105431-{us-east-1,eu-west-2}`    | CDK bootstrap roles (deploy, lookup, file-publishing, cfn-exec, image-publishing) |

## AWS Service-Linked Roles (auto-created, do not delete)

| Role                                                | Service                |
| --------------------------------------------------- | ---------------------- |
| `AWSServiceRoleForResourceExplorer`                 | Resource Explorer      |
| `AWSServiceRoleForSSO`                              | IAM Identity Center    |
| `AWSServiceRoleForElasticLoadBalancing`             | ELB                    |
| `AWSServiceRoleForAmazonGuardDuty`                  | GuardDuty              |
| `AWSServiceRoleForAmazonGuardDutyMalwareProtection` | GuardDuty Malware      |
| `AWSServiceRoleForCloudWatchRUM`                    | CloudWatch RUM         |
| `AWSServiceRoleForBackup`                           | AWS Backup             |
| `AWSServiceRoleForCloudFrontLogger`                 | CloudFront Logger      |
| `AWSServiceRoleForTrustedAdvisor`                   | Trusted Advisor        |
| `AWSServiceRoleForAPIGateway`                       | API Gateway            |
| `AWSServiceRoleForOrganizations`                    | Organizations          |
| `AWSServiceRoleForSupport`                          | AWS Support            |
| `AWSServiceRoleForSecurityHub`                      | Security Hub           |
| `AWSServiceRoleForLambdaReplicator`                 | Lambda@Edge Replicator |

## AWS Default Resources (built-in, cannot delete)

| Resource                                    | Type                     |
| ------------------------------------------- | ------------------------ |
| AppRunner `DefaultConfiguration`            | Auto-scaling config      |
| Athena `AwsDataCatalog`                     | Data catalog             |
| Athena `primary` workgroup                  | Workgroup                |
| ElastiCache `default` user                  | User                     |
| MemoryDB `default` user                     | User                     |
| MemoryDB `open-access` ACL                  | ACL                      |
| MemoryDB parameter groups (6)               | Default parameter groups |
| RDS `default` security group                | Security group           |
| Events `default` bus                        | Event bus                |
| S3 Storage Lens `default-account-dashboard` | Dashboard                |
| Resource Explorer index + view              | Explorer                 |

## Intentional Non-CDK Resources

| Resource               | Purpose                                                                   |
| ---------------------- | ------------------------------------------------------------------------- |
| IAM MFA                | `root-account-mfa-device` — root account MFA                              |
| SAML provider          | `AWSSSO_*_DO_NOT_DELETE` — IAM Identity Center federation                 |
| SSO reserved roles (2) | `AWSReservedSSO_AdministratorAccess_*`, `AWSReservedSSO_ReadOnlyAccess_*` |
| KMS keys (4)           | Encryption keys (likely CDK/S3/CloudFormation)                            |
| SES identities (4)     | Email sending: `support@`, `admin@`, personal addresses                   |
