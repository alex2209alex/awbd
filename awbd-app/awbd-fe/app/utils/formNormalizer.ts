export async function normalizeArrayField<T>(
	items: { [K in keyof T]: any }[],
	fields: Array<{ name: keyof T; type: string; endpoint?: string }>,
): Promise<any[]> {
	// For each entry in the array...
	return Promise.all(
		items.map(async (item) => {
			const result: any = {};
			for (const f of fields) {
				const raw = item[f.name];
				if (f.type === "dropdown") {
					// fetch the full object for that ID:
					const res = await fetch(`${f.endpoint}/${raw}`);
					result[f.name] = await res.json();
				} else {
					result[f.name] = raw;
				}
			}
			return result;
		}),
	);
}

export function denormalizeArrayField<T>(
	items: any[],
	fields: Array<{ name: keyof T; type: string }>,
): any[] {
	return items.map((item) => {
		const result: any = {};
		for (const f of fields) {
			if (f.type === "dropdown") {
				// strip down to primitive ID
				result[f.name] =
					typeof item[f.name] === "object" ? item[f.name].id : item[f.name];
			} else {
				result[f.name] = item[f.name];
			}
		}
		return result;
	});
}
