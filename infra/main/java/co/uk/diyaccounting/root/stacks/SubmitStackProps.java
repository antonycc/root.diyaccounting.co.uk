/*
 * SPDX-License-Identifier: AGPL-3.0-only
 * Copyright (C) 2025-2026 DIY Accounting Ltd
 */

package co.uk.diyaccounting.root.stacks;

import co.uk.diyaccounting.root.SubmitSharedNames;

public interface SubmitStackProps {
    String envName();

    String deploymentName();

    String resourceNamePrefix();

    String cloudTrailEnabled();

    SubmitSharedNames sharedNames();
}
