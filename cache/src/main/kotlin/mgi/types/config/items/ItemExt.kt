package mgi.types.config.items


fun ItemDefinitions.transform(amount :Int = 1): ItemDefinitions {
    if (stackIds != null && amount > 1) {
        var var2 = -1
        for (i in 0 until 10) {
            if (amount > stackAmounts[i] && stackIds[i] != 0)
                var2 = stackIds[i]
        }
        if (var2 != -1)
            return ItemDefinitions.get(var2)
    }
    return this;
}
