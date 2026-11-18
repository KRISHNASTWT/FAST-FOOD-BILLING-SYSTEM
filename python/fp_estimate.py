# Function Point (FP) estimation - Fast Food Billing System (FFBS)   [Experiment 12]
WEIGHTS = {"EI": (3, 4, 6), "EO": (4, 5, 7), "EQ": (3, 4, 6),
           "ILF": (7, 10, 15), "EIF": (5, 7, 10)}      # (simple, average, complex)
LEVEL = {"S": 0, "A": 1, "C": 2}
PRODUCTIVITY = 10                                       # FP per person-month (assumed)

COMPONENTS = [                                          # (type, component, complexity)
    ("EI", "Login and authentication", "S"),
    ("EI", "Add menu item", "A"),
    ("EI", "Edit menu item", "A"),
    ("EI", "Delete menu item", "S"),
    ("EI", "Set item availability", "S"),
    ("EI", "Add / change items in an order", "A"),
    ("EI", "Take payment (cash / card / UPI)", "C"),
    ("EI", "Cancel order", "S"),
    ("EI", "Add cashier account", "S"),
    ("EI", "Deactivate cashier account", "S"),
    ("EI", "Update GST rate and discount rules", "S"),
    ("EO", "Itemized bill / receipt (GST, discount)", "A"),
    ("EO", "Daily sales report", "A"),
    ("EO", "Item-wise sales report", "A"),
    ("EO", "Payment-mode-wise sales report", "S"),
    ("EO", "Cash change display", "S"),
    ("EQ", "View menu with availability", "S"),
    ("EQ", "View cashier accounts", "S"),
    ("ILF", "Users", "S"),
    ("ILF", "Menu_Items", "S"),
    ("ILF", "Orders and Order_Items", "A"),
    ("ILF", "Tax and discount settings", "S"),
    ("EIF", "Payment gateway (card / UPI response)", "S"),
]

GSC = [("Data communications", 4), ("Distributed data processing", 2), ("Performance", 4),
       ("Heavily used configuration", 3), ("Transaction rate", 4), ("On-line data entry", 5),
       ("End-user efficiency", 4), ("On-line update", 4), ("Complex processing", 2),
       ("Reusability", 2), ("Installation ease", 3), ("Operational ease", 3),
       ("Multiple sites", 1), ("Facilitate change", 3)]


def count_table():
    rows = []
    for t in WEIGHTS:
        n = [sum(1 for ct, _, c in COMPONENTS if ct == t and c == k) for k in "SAC"]
        rows.append((t, *n, sum(a * w for a, w in zip(n, WEIGHTS[t]))))
    return rows


if __name__ == "__main__":
    print(f"{'Type':<6}{'Simple':>7}{'Average':>9}{'Complex':>9}{'Weighted':>10}")
    for t, s, a, c, w in count_table():
        print(f"{t:<6}{s:>7}{a:>9}{c:>9}{w:>10}")
    ufp = sum(r[4] for r in count_table())
    sum_fi = sum(v for _, v in GSC)
    vaf = 0.65 + 0.01 * sum_fi
    fp = ufp * vaf
    print(f"\nUnadjusted FP (UFP)    = {ufp}")
    print(f"Sum of GSC ratings     = {sum_fi}")
    print(f"VAF = 0.65 + 0.01 x {sum_fi} = {vaf:.2f}")
    print(f"FP = UFP x VAF         = {ufp} x {vaf:.2f} = {fp:.2f}")
    print(f"Effort = FP / {PRODUCTIVITY}       = {fp / PRODUCTIVITY:.2f} person-months")
