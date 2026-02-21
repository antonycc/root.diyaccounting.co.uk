# Claude Code Memory - DIY Accounting Root

## Context Survival (CRITICAL — read this first after every compaction)

**After compaction or at session start:**
1. Read all `PLAN_*.md` files in the project root — these are the active goals
2. Run `TaskList` to see tracked tasks with status
3. Do NOT start new work without checking these first

**During work:**
- When the user gives a new requirement, add it to the relevant `PLAN_*.md` or create a new one
- Track all user goals as Tasks with status (pending -> in_progress -> completed)
- Update `PLAN_*.md` with progress before context gets large

**PLAN file pattern:**
- Active plans live at project root: `PLAN_<DESCRIPTION>.md`
- Each plan has user assertions verbatim at the top (non-negotiable requirements)
- Plans track problems, fixes applied, and verification criteria
- If no plan file exists for the current work, create one before starting
- Never nest plans in subdirectories — always project root

## Quick Reference

This repository manages the **root AWS account** (887764105431) for diyaccounting.co.uk:
- **Route53 hosted zone** for `diyaccounting.co.uk` (all DNS records)
- **RootDnsStack**: Alias records pointing to gateway/spreadsheets CloudFront distributions
- **Holding page**: Maintenance page at `{env}-holding.diyaccounting.co.uk`
- **Cross-account delegation role**: `root-route53-record-delegate` for submit accounts

**What this repo does NOT have**: Lambda, DynamoDB, Cognito, API Gateway, Docker, ngrok, HMRC, Stripe, or any application code.

## AWS Accounts and Repositories

| Account | ID | Repository | Purpose |
|---------|-----|-----------|---------|
| Management (root) | 887764105431 | `antonycc/root.diyaccounting.co.uk` | **This repo** — Route53, holding page |
| gateway | 283165661847 | `antonycc/www.diyaccounting.co.uk` (future) | Gateway static site |
| spreadsheets | 064390746177 | `antonycc/diy-accounting` (future) | Spreadsheets static site |
| submit-ci | 367191799875 | `antonycc/submit.diyaccounting.co.uk` | Submit CI deployments |
| submit-prod | 972912397388 | `antonycc/submit.diyaccounting.co.uk` | Submit prod deployments |
| submit-backup | 914216784828 | — | Cross-account backup vault |

## Git Workflow

**You may**: create branches, commit changes, push branches, open pull requests

**You may NOT**: merge PRs, push to main, delete branches, rewrite history

**Branch naming**: `claude/<short-description>`

## Build Commands

```bash
npm install                    # Install CDK CLI
./mvnw clean verify            # Build CDK JARs
npm run cdk:synth              # Synthesize CloudFormation templates
npm run cdk:diff               # Show pending changes
```

## CDK Architecture

**Single CDK application** (`cdk-root/`):
- Entry point: `RootEnvironment.java` -> `submit-root.jar`
- Stack: `root-RootDnsStack` (Route53 alias records + delegation role)

**Shared Java utilities** (from submit repo, same package `co.uk.diyaccounting.submit`):
- `Kind.java` — Logging (infof, warnf, envOr)
- `KindCdk.java` — CDK utilities (cfnOutput, buildPrimaryEnvironment, ensureLogGroupWithDependency)
- `Route53AliasUpsert.java` — Idempotent DNS A/AAAA alias records via AwsCustomResource
- `ResourceNameUtils.java` — Name conversion utilities

**SubmitSharedNames.java** is included for compilation compatibility with ApexStack but most of its fields are unused in this repo.

## Formatting

- Spotless with Palantir Java Format (100-column width)
- Runs during Maven `install` phase
- Fix: `./mvnw spotless:apply` (only when asked)

## IAM Best Practices

- Follow least privilege principle
- Avoid `Resource: "*"` wildcards
- Use specific ARNs where possible

## Deployment

Deployments are triggered via GitHub Actions workflows:

| Workflow | Purpose | Trigger |
|----------|---------|---------|
| `deploy.yml` | Deploy RootDnsStack (DNS records) | Manual dispatch |
| `deploy-holding.yml` | Switch apex to holding page or last-known-good | Manual dispatch |

Both workflows use OIDC authentication with these GitHub vars:
- `ROOT_ACTIONS_ROLE_ARN`, `ROOT_DEPLOY_ROLE_ARN` — Root account roles
- `GATEWAY_ACTIONS_ROLE_ARN`, `GATEWAY_DEPLOY_ROLE_ARN` — For CloudFront lookups
- `SPREADSHEETS_ACTIONS_ROLE_ARN`, `SPREADSHEETS_DEPLOY_ROLE_ARN` — For CloudFront lookups
- `ROOT_ACCOUNT_ID`, `ROOT_HOSTED_ZONE_ID` — Account constants

## AWS CLI Access

Use SSO profiles:
```bash
aws sso login --sso-session diyaccounting
aws --profile management route53 list-hosted-zones
aws --profile management cloudformation describe-stacks --stack-name root-RootDnsStack --region us-east-1
```

**Read-only AWS operations are always permitted.** Ask before any write operations.

## AWS Write Operations (CRITICAL)

**ALWAYS ask before writing to AWS.** Any mutating operation (create, update, delete) requires explicit user approval.

## Confirm Means Stop and Wait (CRITICAL)

When the user says "confirm each command" or similar:
1. **Present the command** in a code block.
2. **STOP. Do not execute.** Wait for the user to explicitly approve.
3. Only after the user says "yes", "go ahead", "run it", or similar, execute that single command.
4. Then present the next command and **STOP again**.

## Code Quality Rules

- **No unnecessary formatting** — don't reformat lines you're not changing
- **No import reordering** — considered unnecessary formatting
- **No backwards-compatible aliases** — update all callers consistently
- Only run `./mvnw spotless:apply` when specifically asked

## Security Checklist

- Never commit secrets — use AWS Secrets Manager ARNs
- Check IAM for least privilege (avoid `Resource: "*"`)
- Route53 delegation role is scoped to specific hosted zone only
