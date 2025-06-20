# Architecture Decision Records (ADRs)

This directory contains Architecture Decision Records (ADRs) for the Virtual Game Web App project.

## What are ADRs?

Architecture Decision Records are documents that capture important architectural decisions made along with their context and consequences. Each ADR describes a single architectural decision.

## Why use ADRs?

ADRs help us:
- Document important decisions for future reference
- Communicate architectural choices to new team members
- Understand the reasoning behind past decisions
- Avoid revisiting decisions without awareness of previous considerations

## ADR Structure

Each ADR follows a standard format:
- **Title**: A descriptive title that summarizes the decision
- **Status**: Current status (Proposed, Accepted, Deprecated, Superseded)
- **Context**: The problem statement and background
- **Decision**: The solution chosen
- **Consequences**: The resulting trade-offs and impacts
- **Alternatives Considered**: Other options that were evaluated
- **References**: Related resources and documentation

## Creating a New ADR

To create a new ADR:

1. Copy the template file `0000-template.md` to a new file named `NNNN-descriptive-title.md` where `NNNN` is the next available number in sequence
2. Fill in the sections of the template
3. Submit the ADR for review through a pull request

## ADR Index

| Number | Title | Status |
|--------|-------|--------|
| [0001](0001-distributed-tracing.md) | Implement Distributed Tracing with Micrometer Tracing and Zipkin | Accepted |
| [0002](0002-architecture-decision-records.md) | Use Architecture Decision Records (ADRs) | Accepted |

## References

- [Documenting Architecture Decisions by Michael Nygard](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)
- [ADR GitHub organization](https://adr.github.io/)
