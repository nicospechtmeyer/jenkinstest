package de.polipol.analytics.commons.io;

import org.apache.commons.lang3.StringUtils;

public final class TexFormattingStrategy implements FormattingStrategy {

	@Override
	public String formatString(String value) {
		value = StringUtils.trim(value);

		value = StringUtils.replace(value, "-", "-");
		value = StringUtils.replace(value, "–", "--");
		value = StringUtils.replace(value, "—", "---");
		value = StringUtils.replace(value, "…", "\\dots");
		value = StringUtils.replace(value, "⋮", "\\vdots");
		value = StringUtils.replace(value, "’", "'");
		value = StringUtils.replace(value, "´", "'");

		value = StringUtils.replace(value, "&", "\\&");
		value = StringUtils.replace(value, "{", "\\{");
		value = StringUtils.replace(value, "}", "\\}");
		value = StringUtils.replace(value, "$", "\\$");
		value = StringUtils.replace(value, "#", "\\#");
		value = StringUtils.replace(value, "°", "\\textdegree");
		value = StringUtils.replace(value, "^", "\\^{}");
		value = StringUtils.replace(value, "_", "\\_");
		value = StringUtils.replace(value, "~", "\\textasciitilde");
		value = StringUtils.replace(value, "%", "\\%");

		value = StringUtils.replace(value, "Γ", "\\Gamma");
		value = StringUtils.replace(value, "Δ", "\\Delta");
		// value = StringUtils.replace(value, "Θ", "\\Theta");
		value = StringUtils.replace(value, "Λ", "\\Lambda");
		// value = StringUtils.replace(value, "Ξ", "\\Xi");
		value = StringUtils.replace(value, "Π", "\\Pi");
		value = StringUtils.replace(value, "Φ", "\\Phi");
		value = StringUtils.replace(value, "Ψ", "\\Psi");
		value = StringUtils.replace(value, "α", "\\alpha");
		value = StringUtils.replace(value, "β", "\\beta");
		value = StringUtils.replace(value, "γ", "\\gamma");
		value = StringUtils.replace(value, "δ", "\\delta");
		value = StringUtils.replace(value, "ϵ", "\\epsilon");
		value = StringUtils.replace(value, "ζ", "\\zeta");
		value = StringUtils.replace(value, "η", "\\eta");
		value = StringUtils.replace(value, "θ", "\\theta");
		value = StringUtils.replace(value, "ι", "\\iota");
		value = StringUtils.replace(value, "κ", "\\kappa");
		value = StringUtils.replace(value, "λ", "\\lambda");
		value = StringUtils.replace(value, "μ", "\\mu");
		value = StringUtils.replace(value, "ν", "\\nu");
		value = StringUtils.replace(value, "ξ", "\\xi");
		value = StringUtils.replace(value, "π", "\\pi");
		value = StringUtils.replace(value, "�?", "\\rho");
		value = StringUtils.replace(value, "σ", "\\sigma");
		value = StringUtils.replace(value, "τ", "\\tau");
		value = StringUtils.replace(value, "υ", "\\upsilon");
		value = StringUtils.replace(value, "ϕ", "\\phi");
		value = StringUtils.replace(value, "χ", "\\chi");
		value = StringUtils.replace(value, "ψ", "\\psi");
		value = StringUtils.replace(value, "ω", "\\omega");

		return value;
	}
}
