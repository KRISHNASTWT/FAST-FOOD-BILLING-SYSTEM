# PERT / CPM schedule - Fast Food Billing System   [Experiment 13]
from math import sqrt, erf

# id: (activity, optimistic, most likely, pessimistic, predecessors)   [weeks]
TASKS = {
    "A": ("Requirement analysis & SRS", 1, 2, 3, []),
    "B": ("System design (UML, DB)", 2, 3, 4, ["A"]),
    "C": ("Database creation", 1, 2, 3, ["B"]),
    "D": ("Menu & order module", 2, 4, 6, ["B"]),
    "E": ("Billing & tax module", 2, 3, 4, ["D"]),
    "F": ("Payment & receipt module", 1, 3, 5, ["E"]),
    "G": ("User interface", 2, 4, 6, ["B"]),
    "H": ("Integration", 1, 2, 3, ["C", "F", "G"]),
    "I": ("Testing", 2, 3, 4, ["H"]),
    "J": ("Deployment & training", 0.5, 1, 1.5, ["I"]),
}
order = list(TASKS)                      # already in dependency order
succ = {t: [s for s in order if t in TASKS[s][4]] for t in order}

# PERT three-point estimate: te = (O + 4M + P) / 6, variance = ((P - O) / 6)^2
te = {t: (o + 4 * m + p) / 6 for t, (_, o, m, p, _) in TASKS.items()}
var = {t: ((p - o) / 6) ** 2 for t, (_, o, m, p, _) in TASKS.items()}

# Forward pass
ES, EF = {}, {}
for t in order:
    ES[t] = max((EF[p] for p in TASKS[t][4]), default=0)
    EF[t] = ES[t] + te[t]
duration = max(EF.values())

# Backward pass
LS, LF = {}, {}
for t in reversed(order):
    LF[t] = min((LS[s] for s in succ[t]), default=duration)
    LS[t] = LF[t] - te[t]
slack = {t: round(LF[t] - EF[t], 6) for t in order}
critical = [t for t in order if slack[t] == 0]

print(f"{'ID':<3}{'Activity':<27}{'O':>4}{'M':>4}{'P':>4}{'te':>6}"
      f"{'ES':>5}{'EF':>5}{'LS':>5}{'LF':>5}{'Slack':>6} Critical")
for t in order:
    name, o, m, p, _ = TASKS[t]
    print(f"{t:<3}{name:<27}{o:>4g}{m:>4g}{p:>4g}{te[t]:>6.2f}"
          f"{ES[t]:>5g}{EF[t]:>5g}{LS[t]:>5g}{LF[t]:>5g}{slack[t]:>6g}   "
          f"{'YES' if t in critical else ''}")

sigma = sqrt(sum(var[t] for t in critical))
target = 23
z = (target - duration) / sigma
prob = 0.5 * (1 + erf(z / sqrt(2)))
print(f"\nCritical path : {' -> '.join(critical)}")
print(f"Project duration = {duration:g} weeks, sigma = {sigma:.2f}")
print(f"P(finish within {target} weeks) = {prob:.1%}")
