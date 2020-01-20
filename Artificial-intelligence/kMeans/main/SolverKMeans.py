from sys import argv
from matplotlib.pyplot import scatter, show
from numpy import average
from math import sqrt


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return 'Point: ' + str(self.x) + ' ' + str(self.y)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __hash__(self):
        return hash(self.x + self.y)

    def calculate_distance(self, other):
        return sqrt((self.x - other.x)**2 + (self.y - other.y)**2)


class DataHandler:

    @staticmethod
    def extract_data(path):
        result = []

        dataset_file = open(path, 'r')
        for line in list(dataset_file):
            split = line.split('\t')
            if len(split) == 1:
                split = split[0].split(' ')

            result.append(Point(float(split[0]), float(split[1])))

        dataset_file.close()
        return result


class KMeans:
    def __init__(self, clusters_count, file_path, max_iterations=500):
        self.clusters_count = clusters_count
        self.max_iterations = max_iterations
        self.data = DataHandler.extract_data(file_path)
        self.centroids = {i: self.data[i] for i in range(self.clusters_count)}

    @property
    def find_clusters(self):
        clusters = {}
        for i in range(self.max_iterations):
            clusters = {}
            for j in range(self.clusters_count):
                clusters[j] = []

            for point in self.data:
                distances_from_centroids = [point.calculate_distance(centroid) for centroid in self.centroids.values()]
                cluster = distances_from_centroids.index(min(distances_from_centroids))
                clusters[cluster].append(point)

            for cluster in clusters:
                average_centroid = Point(0, 0)
                average_centroid.x = average([point.x for point in clusters[cluster]])
                average_centroid.y = average([point.y for point in clusters[cluster]])
                self.centroids[cluster] = average_centroid

            optimal = True

            for centroid_index in range(self.clusters_count):
                for cluster in clusters[centroid_index]:
                    if type(cluster) != 'list':
                        cluster = [cluster]
                    for point in cluster:
                        if self._get_min_dist_to_centroid(point) != self.centroids[centroid_index]:
                            optimal = False

            if optimal:
                break

        return clusters

    def _get_min_dist_to_centroid(self, point):
        result = None

        for centroid in self.centroids.keys():
            if result is None:
                result = self.centroids[centroid]
            else:
                if point.calculate_distance(self.centroids[centroid]) < point.calculate_distance(result):
                    result = self.centroids[centroid]

        return result

    def plot_clusters(self, clusters):
        colors = 10 * ["r", "g", "c", "b", "k"]

        for centroid in self.centroids:
            scatter(self.centroids[centroid].x, self.centroids[centroid].y, s=130, marker="x")

        for classification in clusters:
            color = colors[classification]
            for point in clusters[classification]:
                scatter(point.x, point.y, color=color, s=30)

        show()


def main():
    file_path = argv[1]
    clusters_count = argv[2]

    kMeans = KMeans(int(clusters_count), file_path)
    clusters = kMeans.find_clusters
    kMeans.plot_clusters(clusters)


if __name__ == '__main__':
    main()
