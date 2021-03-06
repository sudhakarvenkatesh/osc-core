/*******************************************************************************
 * Copyright (c) Intel Corporation
 * Copyright (c) 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.osc.core.broker.model.entities.virtualization;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.osc.core.broker.model.entities.BaseEntity;
import org.osc.core.broker.model.entities.appliance.VirtualSystem;
import org.osc.core.broker.model.entities.management.Policy;

@SuppressWarnings("serial")
@Entity
@Table(name = "SECURITY_GROUP_INTERFACE")
public class SecurityGroupInterface extends BaseEntity {

	public static class SecurityGroupInterfaceOrderComparator implements Comparator<SecurityGroupInterface> {

		@Override
		public int compare(SecurityGroupInterface o1, SecurityGroupInterface o2) {
			return Long.compare(o1.getOrder(), o2.getOrder());
		}
	}

	public static final String ISC_TAG_PREFIX = "isc-";

	@Column(name = "name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "virtual_system_fk", nullable = false, foreignKey = @ForeignKey(name = "FK_SG_VIRTUAL_SYSTEM"))
	private VirtualSystem virtualSystem;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SECURITY_GROUP_INTERFACE_POLICY",
			joinColumns = @JoinColumn(name = "sgi_fk", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "policy_fk", referencedColumnName = "id"))
	private Set<Policy> policies = new HashSet<>();

	/**
	 * The tag is assumed to be in the format "SOMESTRING" "-" "LONG VALUE".
	 * isc-456 for example.
	 */
	@Column(name = "tag", nullable = true)
	private String tag;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "security_group_fk", nullable = true, foreignKey = @ForeignKey(name = "FK_SECURITY_GROUP"))
	private SecurityGroup securityGroup;

	@Column(name = "user_configurable", columnDefinition = "bit default 0")
	private boolean isUserConfigurable;

	@Column(name = "mgr_interface_id")
	private String mgrSecurityGroupInterfaceId;

	@Column(name = "mgr_security_group_id")
	private String mgrSecurityGroupId;

	@Column(name = "failure_policy_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private FailurePolicyType failurePolicyType = FailurePolicyType.NA;

	@Column(name = "chain_order", columnDefinition = "bigint default 0", nullable = false)
	private Long order = 0L;

	/**
	 * Represents the identifier of the inspection hook created in the SDN controller
	 * when it supports port groups.
	 */
	@Column(name = "network_elem_id")
	private String networkElementId;

	public SecurityGroupInterface(VirtualSystem virtualSystem, Set<Policy> policies, String tag,
			FailurePolicyType failurePolicyType, Long order) {
		super();
		this.virtualSystem = virtualSystem;
		this.policies = policies;
		this.tag = tag;
		this.isUserConfigurable = true;
		this.failurePolicyType = failurePolicyType;
		this.order = order;
	}

	public SecurityGroupInterface() {
		super();
	}

	public Set<Policy> getPolicies() {
		return this.policies;
	}

	public void setPolicies(Set<Policy> policies) {
		this.policies = policies;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getTagValue() {
		return this.tag == null ? null : Long.valueOf(this.tag.substring(this.tag.indexOf("-") + 1));
	}

	public Set<String> getMgrPolicyIds() {
		return this.policies == null ? Collections.emptySet()
				: this.policies.stream().map(Policy::getMgrPolicyId).collect(Collectors.toSet());
	}

	public Set<Policy> getMgrPolicies() {
		return this.policies;
	}

	public VirtualSystem getVirtualSystem() {
		return this.virtualSystem;
	}

	public void setUserConfigurable(boolean isUserConfigurable) {
		this.isUserConfigurable = isUserConfigurable;
	}

	public boolean isUserConfigurable() {
		return this.isUserConfigurable;
	}

	public String getMgrSecurityGroupInterfaceId() {
		return this.mgrSecurityGroupInterfaceId;
	}

	public void setMgrSecurityGroupInterfaceId(String mgrSecurityGroupIntefaceId) {
		this.mgrSecurityGroupInterfaceId = mgrSecurityGroupIntefaceId;
	}

	public String getMgrSecurityGroupId() {
		return this.mgrSecurityGroupId;
	}

	public void setMgrSecurityGroupId(String mgrSecurityGroupId) {
		this.mgrSecurityGroupId = mgrSecurityGroupId;
	}

	public FailurePolicyType getFailurePolicyType() {
		return this.failurePolicyType;
	}

	public void setFailurePolicyType(FailurePolicyType failurePolicyType) {
		this.failurePolicyType = failurePolicyType;
	}

	public void setSecurityGroup(SecurityGroup securityGroup) {
		this.securityGroup = securityGroup;
	}

	public SecurityGroup getSecurityGroup() {
		return this.securityGroup;
	}

	public String getSecurityGroupInterfaceId() {
		return this.mgrSecurityGroupInterfaceId;
	}

	public Long getOrder() {
		return this.order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public String getNetworkElementId() {
		return this.networkElementId;
	}

	public void setNetworkElementId(String networkElemId) {
		this.networkElementId = networkElemId;
	}
}
